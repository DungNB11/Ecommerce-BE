package com.dungnb.be.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.function.Function;

@Slf4j
@Component
public class SlugUtils {
    public String generateSlug(String text) {
        if (text == null || text.trim()
                .isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        log.debug("Generating slug from: {}", text);

        // Step 1: Convert to lowercase
        String slug = text.toLowerCase()
                .trim();

        // Step 2: Remove Vietnamese accents
        slug = removeVietnameseAccents(slug);

        // Step 3: Replace spaces and special characters with hyphens
        slug = slug.replaceAll("\\s+", "-");           // Replace spaces with -
        slug = slug.replaceAll("[^a-z0-9-]", "");      // Keep only a-z, 0-9, and -
        slug = slug.replaceAll("-+", "-");             // Replace multiple - with single -
        slug = slug.replaceAll("^-|-$", "");           // Remove leading/trailing -

        log.debug("Generated slug: {}", slug);
        return slug;
    }

    public String generateUniqueSlug(String text, Function<String, Boolean> existsChecker) {
        String baseSlug = generateSlug(text);
        String slug = baseSlug;
        int counter = 2;

        log.debug("Checking uniqueness for slug: {}", slug);

        while (existsChecker.apply(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
            log.debug("Slug exists, trying: {}", slug);
        }

        log.info("Generated unique slug: {}", slug);
        return slug;
    }

    public String generateUniqueSlug(String text, String currentSlug, Function<String, Boolean> existsChecker) {
        String newSlug = generateSlug(text);

        // If new slug is same as current slug, no need to check
        if (newSlug.equals(currentSlug)) {
            log.debug("New slug is same as current slug, no change needed");
            return newSlug;
        }

        // Otherwise, ensure uniqueness
        return generateUniqueSlug(text, existsChecker);
    }

    private String removeVietnameseAccents(String text) {
        if (text == null) return null;

        // Map of Vietnamese characters to non-accented equivalents
        text = text.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        text = text.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        text = text.replaceAll("[ìíịỉĩ]", "i");
        text = text.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        text = text.replaceAll("[ùúụủũưừứựửữ]", "u");
        text = text.replaceAll("[ỳýỵỷỹ]", "y");
        text = text.replaceAll("đ", "d");

        // Normalize remaining accents
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");

        return text;
    }
}
