package net.draycia.carbon.common.adventure;

import de.themoep.minedown.adventure.MineDown;
import net.draycia.carbon.api.adventure.MessageProcessor;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AdventureManager implements MessageProcessor {

  @NonNull
  private final AudienceProvider provider;

  @NonNull
  private final FormatType formatType;

  public AdventureManager(final @NonNull AudienceProvider provider, final @NonNull FormatType formatType) {
    this.provider = provider;
    this.formatType = formatType;
  }

  @Override
  public @NonNull Component processMessage(@Nullable final String input, final @NonNull String @NonNull ... placeholders) {
    if (input == null || input.trim().isEmpty()) {
      return TextComponent.empty();
    }

    switch (this.formatType) {
      case MINEDOWN:
        return MineDown.parse(input, placeholders);
      case MOJANG:
        return this.processMojang(input, placeholders);
      case MINIMESSAGE_MARKDOWN:
        return MiniMessage.markdown().parse(input, placeholders);
      case MINIMESSAGE:
      default:
        return MiniMessage.get().parse(input, placeholders);
    }
  }

  private @NonNull Component processMojang(@NonNull String input, final @NonNull String @NonNull ... placeholders) {
    for (int i = 0; i < placeholders.length; i += 2) {
      final String placeholder = placeholders[i];
      final String replacement = placeholders[i + 1];

      input = input.replace("<" + placeholder + ">", replacement);
    }

    return this.audiences().gsonSerializer().deserialize(input);
  }

  @Override
  public @NonNull AudienceProvider audiences() {
    return this.provider;
  }
}