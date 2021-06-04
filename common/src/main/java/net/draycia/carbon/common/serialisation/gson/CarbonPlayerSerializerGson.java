package net.draycia.carbon.common.serialisation.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.draycia.carbon.api.users.CarbonPlayer;

public class CarbonPlayerSerializerGson implements JsonSerializer<CarbonPlayer>, JsonDeserializer<CarbonPlayer> {

    @Override
    public CarbonPlayer deserialize(
        final JsonElement jsonElement,
        final Type type,
        final JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(
        final CarbonPlayer player,
        final Type type,
        final JsonSerializationContext context
    ) {
        final JsonObject object = new JsonObject();

        object.add("displayName", context.serialize(player.displayName()));
        object.add("identity", context.serialize(player.identity()));
        object.add("selectedChannel", context.serialize(player.selectedChannel()));
        object.add("username", context.serialize(player.username()));
        object.add("uuid", context.serialize(player.uuid()));

        return object;
    }

}