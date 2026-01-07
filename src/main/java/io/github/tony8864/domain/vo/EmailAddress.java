package io.github.tony8864.domain.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record EmailAddress(String value) {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public EmailAddress {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email Address cannot be null or blank");
        }
        Matcher mather = VALID_EMAIL_ADDRESS_REGEX.matcher(value);
        if (!mather.matches()) {
            throw new IllegalArgumentException("Invalid Email Address format");
        }
    }

    public static EmailAddress newEmail(String value) {
        return new EmailAddress(value);
    }
}
