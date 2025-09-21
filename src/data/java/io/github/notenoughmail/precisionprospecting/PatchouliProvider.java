package io.github.notenoughmail.precisionprospecting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dries007.tfc.util.Helpers;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PatchouliProvider implements Provider, DataProvider {

    protected static JsonObject json(Consumer<JsonObject> builder) {
        return Util.make(new JsonObject(), builder);
    }

    protected static void ifNotNull(@Nullable String val, String key, JsonObject obj) {
        if (val != null) {
            obj.addProperty(key, val);
        }
    }
    
    protected PackOutput.PathProvider out;
    protected CompletableFuture<HolderLookup.Provider> lookupProvider;
    protected ExistingFileHelper efh;

    final List<EncodableContent> entries = new ArrayList<>();
    
    @Override
    public void add(Consumer<? super DataProvider> ret, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh) {
        out = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "patchouli_books/field_guide/en_us");
        this.lookupProvider = lookupProvider;
        this.efh = efh;
        ret.accept(this);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return lookupProvider.thenCompose(provider -> {
            PatchiData.generate(entries::add);
            return CompletableFuture.allOf(
                    entries.stream()
                            .map(e -> DataProvider.saveStable(output, e.encode(), out.json(e.id())))
                            .toArray(CompletableFuture[]::new)
            );
        });
    }

    @Override
    public String getName() {
        return "PatchouliPageProvider";
    }
    
    interface EncodableContent {

        ResourceLocation id();

        JsonElement encode();
    }

    public static class Category implements EncodableContent {

        final String file, name, description;
        final ResourceLocation categoryId;
        public String icon, parent, flag;
        public int sortNum;
        public boolean secret;

        public Category(String file, String name, String description, Consumer<Category> builder) {
            this.file = file;
            this.name = name;
            this.description = description;
            categoryId = Helpers.identifier(file);
            builder.accept(this);
        }

        public Category icon(ItemLike item) {
            icon = BuiltInRegistries.ITEM.getKey(item.asItem()).toString();
            return this;
        }

        @Override
        public ResourceLocation id() {
            return Helpers.identifier("categories/" + file);
        }

        @Override
        public JsonElement encode() {
            Objects.requireNonNull(icon);
            return json(j -> {
                ifNotNull(name, "name", j);
                ifNotNull(description, "description", j);
                ifNotNull(icon, "icon", j);
                ifNotNull(parent, "parent", j);
                ifNotNull(flag, "flag", j);
                if (sortNum != 0) {
                    j.addProperty("sortnum", sortNum);
                }
                if (secret) {
                    j.addProperty("secret", true);
                }
            });
        }
    }

    public static class Entry implements EncodableContent {

        final Category category;
        final String name, file;
        final Map<ItemLike, Integer> recipeMappings = new HashMap<>();
        final List<Page> pages = new ArrayList<>();
        public String icon;
        public boolean readByDefault, priority, secret;
        public int sortNum;

        public Entry(String file, String name, Category category, Consumer<Entry> builder) {
            this.file = file;
            this.name = name;
            this.category = category;
            builder.accept(this);
        }

        public Entry icon(ItemLike item) {
            icon = BuiltInRegistries.ITEM.getKey(item.asItem()).toString();
            return this;
        }

        private <T extends Page> T add(T t) {
            pages.add(t);
            return t;
        }

        public TextPage textPage() {
            return add(new TextPage(this));
        }

        public SpotlightPage spotlight(boolean linkRecipes, ItemLike item) {
            return add(new SpotlightPage(linkRecipes, item.asItem(), this));
        }

        public RecipePage recipe(String recipe) {
            return add(new RecipePage(recipe, this));
        }

        public Entry recipeMapping(ItemLike item, int page) {
            recipeMappings.put(item, page);
            return this;
        }

        @Override
        public ResourceLocation id() {
            return category.categoryId.withPrefix("entries/").withSuffix("/" + file);
        }

        public String linkLocation() {
            return category.file + "/" + file;
        }

        @Override
        public JsonElement encode() {
            return json(j -> {
                ifNotNull(category.categoryId.toString(), "category", j);
                ifNotNull(icon, "icon", j);
                ifNotNull(name, "name", j);
                if (priority) j.addProperty("priority", true);
                if (secret) j.addProperty("secret", true);
                if (readByDefault) j.addProperty("read_by_default", true);
                if (sortNum != 0) j.addProperty("sortnum", sortNum);
                final JsonArray arr = new JsonArray(pages.size());
                pages.forEach(s -> arr.add(s.build()));
                j.add("pages", arr);
                if (!recipeMappings.isEmpty()) {
                    j.add("extra_recipe_mappings", json(obj -> recipeMappings.forEach((i, n) -> obj.addProperty(BuiltInRegistries.ITEM.getKey(i.asItem()).toString(), n))));
                }
            });
        }
    }

    public static abstract class Page {

        String anchor;
        private final Entry entry;

        protected Page(Entry entry) {
            this.entry = entry;
        }

        public TextPage textPage() {
            return entry.textPage();
        }

        public SpotlightPage spotlight(boolean linkRecipes, ItemLike item) {
            return entry.spotlight(linkRecipes, item);
        }

        public RecipePage recipe(String recipe) {
            return entry.recipe(recipe);
        }

        public Entry exit() {
            return entry;
        }

        abstract String type();

        abstract void populate(JsonObject obj);

        public Page anchor(String anchor) {
            this.anchor = anchor;
            return this;
        }

        JsonObject build() {
            return json(j -> {
                j.addProperty("type", type());
                ifNotNull(anchor, "anchor", j);
                populate(j);
            });
        }
    }

    public static class TextPage extends Page {

        final StringBuilder builder = new StringBuilder();
        String title;

        public TextPage(Entry entry) {
            super(entry);
        }

        @Override
        public String type() {
            return "patchouli:text";
        }

        public TextPage title(String title) {
            this.title = title;
            return this;
        }

        public TextPage text(String text) {
            builder.append(text);
            return this;
        }

        public TextPage br() {
            return text("$(br)");
        }

        public TextPage li() {
            return text("$(li)");
        }

        public TextPage thing(String thing) {
            builder.append("$(thing)");
            builder.append(thing);
            return text("$()");
        }

        public TextPage link(String entry, @Nullable String anchor, String text) {
            builder.append("$(l:");
            builder.append(entry);
            if (anchor != null) {
                builder.append('#');
                builder.append(anchor);
            }
            builder.append(')');
            builder.append(text);
            return text("$()");
        }

        public TextPage link(Entry entry, @Nullable String anchor, String text) {
            return link(entry.linkLocation(), anchor, text);
        }

        @Override
        public void populate(JsonObject obj) {
            obj.addProperty("text", builder.toString());
            ifNotNull(title, "title", obj);
        }
    }

    public static class SpotlightPage extends TextPage {

        final boolean linkRecipes;
        final Item item;

        public SpotlightPage(boolean linkRecipes, Item item, Entry entry) {
            super(entry);
            this.linkRecipes = linkRecipes;
            this.item = item;
        }

        @Override
        public String type() {
            return "patchouli:spotlight";
        }

        @Override
        public void populate(JsonObject obj) {
            super.populate(obj);
            obj.addProperty("link_recipe", linkRecipes);
            obj.addProperty("item", BuiltInRegistries.ITEM.getKey(item).toString());
        }
    }

    public static class RecipePage extends TextPage {

        final String recipe;
        String type;

        public RecipePage(String recipe, Entry entry) {
            super(entry);
            this.recipe = recipe;
        }

        public RecipePage knap() {
            type = "tfc:knapping_recipe";
            return this;
        }

        public RecipePage craft() {
            type = "patchouli:crafting";
            return this;
        }

        public RecipePage anvil() {
            type = "tfc:anvil_recipe";
            return this;
        }

        @Override
        public String type() {
            return type;
        }

        @Override
        public void populate(JsonObject obj) {
            super.populate(obj);
            obj.addProperty("recipe", recipe);
        }
    }
}
