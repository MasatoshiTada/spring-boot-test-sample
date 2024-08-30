package com.example.integration.rest;

public record SessionTokenPair(String sessionId, String csrfToken) {
}
