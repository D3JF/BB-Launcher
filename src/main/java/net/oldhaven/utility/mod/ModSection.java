package net.oldhaven.utility.mod;

import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.List;

public class ModSection {
    private List<Mod> mods;
    private String name;

    public ModSection(String name) {
        mods = new LinkedList<>();
        this.name = name;
    }

    public Mod addMod(ModType type, String name, String path, boolean defaultEnabled) {
        Mod mod = new Mod(type,this, name, path);
        if(Mods.config.getJson() == null || Mods.config.getJson().isJsonNull()) {
            mod.setEnabled(defaultEnabled);
            Mods.shouldUpdate = true;
        } else if(Mods.config.getJson().get(this.name) != null) {
            JsonObject self = Mods.config.getJson().get(this.name).getAsJsonObject();
            if (self == null || !self.has(name)) {
                mod.setEnabled(defaultEnabled);
                Mods.shouldUpdate = true;
            } else {
                JsonObject object = ((JsonObject) self.get(name));
                boolean enabled = object.get("Enabled").getAsBoolean();
                mod.setEnabled(enabled);
            }
        } else {
            mod.setEnabled(defaultEnabled);
            Mods.shouldUpdate = true;
        }
        mods.add(mod);
        Mods.mods.add(mod);
        return mod;
    }

    public String getName() {
        return name;
    }
    public List<Mod> getMods() {
        return mods;
    }

    public Mod getModByName(String name) {
        for(Mod mod : mods) {
            if(mod.getName().equalsIgnoreCase(name))
                return mod;
        }
        return null;
    }
    public boolean removeMod(String name) {
        Mod mod = this.getModByName(name);
        if(mod != null) {
            Mods.removeMod(mod);
            mods.remove(mod);
            return true;
        }
        return false;
    }

    JsonObject toJson() {
        JsonObject self = new JsonObject();
        for(Mod mod : mods) {
            JsonObject modJson = new JsonObject();
            modJson.addProperty("Path", mod.getFile().getAbsolutePath());
            modJson.addProperty("Enabled", mod.isEnabled());
            modJson.addProperty("Type", mod.getType().name());
            self.add(mod.getName(), modJson);
        }
        return self;
    }
}
