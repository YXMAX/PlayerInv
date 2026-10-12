package com.playerinv.util.exception;

import com.playerinv.util.NodeUtil;

public class InvalidVaultAmountException extends Exception {

    public InvalidVaultAmountException(String file_name,String path) {
        NodeUtil.sendMenuError("Invalid vault id: " + file_name + ".yml/" + path);
    }
}
