package com.example.integration.rest;

public record LoginResult(String sessionId, String csrfToken) {
}
