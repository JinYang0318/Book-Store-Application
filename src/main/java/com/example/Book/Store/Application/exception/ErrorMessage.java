package com.example.Book.Store.Application.exception;

import lombok.Builder;

@Builder
public record ErrorMessage(String message) {
}
