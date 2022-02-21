package com.bitdubai.fermat_bch_api.layer.crypto_network.util;

/**
 * Represents a bitcoin node that we are connecte to.
 */
public class ConnectedBitcoinNode {
    private String address;
    private String version;
    private Boolean isDownloader;
    private long pingTime;

    /**
     * Constructor
     * @param address the ip address
     * @param version the running version of the connected client
     * @param isDownloader indicates if this node is the one we are downloading from
     * @param pingTime the duration of the last ping we executed.
     */
    public ConnectedBitcoinNode(String address, String version, Boolean isDownloader, long pingTime) {
        this.address = address;
        this.version = version;
        this.isDownloader = isDownloader;
        this.pingTime = pingTime;
    }

    @Override
    public String toString() {
        return "ConnectedBitcoinNode{" +
                "address='" + address + '\'' +
                ", version='" + version + '\'' +
                ", isDownloader=" + isDownloader +
                ", pingTime=" + pingTime +
                '}';
    }

    /**
     * the ip address
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * the running version of the connected client
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * the duration of the last ping we executed
     * @return
     */
    public long getPingTime() {
        return pingTime;
    }

    /**
     * Indicates if this node is the chosen one to download from
     * @return
     */
    public Boolean getIsDownloader() {
        return isDownloader;
    }
}
