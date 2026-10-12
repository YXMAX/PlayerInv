package com.playerinv.util.exception;

import com.playerinv.util.NodeUtil;

public class InvalidConfigSectionException extends RuntimeException {

    public InvalidConfigSectionException(String message) {
        NodeUtil.sendMenuError("Can't find root section: " + message);
    }
}
