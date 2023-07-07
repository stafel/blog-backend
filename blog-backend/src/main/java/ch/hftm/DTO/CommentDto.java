package ch.hftm.DTO;

import ch.hftm.Entities.BlogUser;

public record CommentDto(String text, BlogUser author) { }
