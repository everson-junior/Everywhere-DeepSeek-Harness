package com.deepseek.everywhere.model;

/**
 * Representa os estados possíveis do runtime EveryWhere Harness.
 */
public enum HarnessStatus {
    CHECKING,
    NOT_INSTALLED,
    INSTALLING,
    STOPPED,
    STARTING,
    RUNNING,
    ERROR
}
