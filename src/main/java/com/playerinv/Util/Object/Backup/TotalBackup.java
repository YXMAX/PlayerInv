package com.playerinv.Util.Object.Backup;

import com.playerinv.Util.NodeUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TotalBackup implements Serializable {

    private static final long serialVersionUID = 22L;

    private List<VaultBackup> vaultBackups;

    public TotalBackup() {
        this.vaultBackups = new ArrayList<VaultBackup>();
    }

    public List<VaultBackup> getVaultBackups() {
        return vaultBackups;
    }

    public void setVaultBackups(List<VaultBackup> vaultBackups) {
        this.vaultBackups = vaultBackups;
    }

    public String saveToString(){
        return NodeUtil.objectToString(this);
    }
}
