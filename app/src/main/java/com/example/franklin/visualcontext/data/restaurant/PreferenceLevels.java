package com.example.franklin.visualcontext.data.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Contains preset preference levels for a certain ingredient.
 */
@AllArgsConstructor
@Getter
public enum PreferenceLevels {

    LIKE_STRONGLY(2),

    LIKE(1),

    NEUTRAL(0),

    DISLIKE(-1),

    DISLIKE_STRONGLY(-2);

    /**
     * Score associated with the preference level. Higher means more preferred
     */
    private Integer score;
}
