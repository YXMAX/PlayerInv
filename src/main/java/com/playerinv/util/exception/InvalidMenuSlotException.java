package com.playerinv.util.exception;

import com.playerinv.util.NodeUtil;

public class InvalidMenuSlotException extends Exception {

    public InvalidMenuSlotException(String file_name, String path) {
        NodeUtil.sendMenuError("Invalid menu slot: " + file_name + ".yml/" + path);
    }
}
