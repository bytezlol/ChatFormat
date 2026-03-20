package wtf.bytezlol.utility;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public final class ColorUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final LegacyComponentSerializer SECTION  = LegacyComponentSerializer.legacySection();
    private static final LegacyComponentSerializer AMPERSAND = LegacyComponentSerializer.legacyAmpersand();
    private static final Pattern HEX_AMPERSAND = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern HEX_BUKKIT    = Pattern.compile("&x(&[A-Fa-f0-9]){6}");

    public static String toMiniMessage(String input) {
        if (input == null) return "";

        input = convertAmpersandHex(input);
        input = convertBukkitHex(input);
        input = input.replace('&', '§');
        input = normalizeLegacySegments(input);

        return input;
    }

    public static Component parse(final String input) {
        return MINI_MESSAGE.deserialize(toMiniMessage(input));
    }

    private static String convertAmpersandHex(String input) {
        final Matcher m = HEX_AMPERSAND.matcher(input);
        final StringBuffer sb = new StringBuffer();
        final char c = '§';
        while (m.find()) {
            final String hex = m.group(1);
            m.appendReplacement(sb, c + "x"
                    + c + hex.charAt(0) + c + hex.charAt(1)
                    + c + hex.charAt(2) + c + hex.charAt(3)
                    + c + hex.charAt(4) + c + hex.charAt(5));
        }
        return m.appendTail(sb).toString();
    }

    private static String convertBukkitHex(String input) {
        final Matcher m = HEX_BUKKIT.matcher(input);
        final StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, Matcher.quoteReplacement(m.group().replace('&', '§')));
        }
        return m.appendTail(sb).toString();
    }

    private static String normalizeLegacySegments(final String input) {
        final Pattern tagPattern = Pattern.compile("</?[a-zA-Z#0-9_:!][^>]*>");
        final Matcher m = tagPattern.matcher(input);

        final StringBuilder result = new StringBuilder();
        int last = 0;

        while (m.find()) {
            final String before = input.substring(last, m.start());
            result.append(legacyToMiniMessage(before));
            result.append(m.group());
            last = m.end();
        }

        result.append(legacyToMiniMessage(input.substring(last)));

        return result.toString();
    }

    private static String legacyToMiniMessage(final String segment) {
        if (segment.isEmpty() || !segment.contains("§")) return segment;
        return MINI_MESSAGE.serialize(SECTION.deserialize(segment));
    }
}