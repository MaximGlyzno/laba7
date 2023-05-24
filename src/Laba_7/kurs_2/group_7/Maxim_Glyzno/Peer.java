package Laba_7.kurs_2.group_7.Maxim_Glyzno;

import java.net.InetSocketAddress;

public class Peer {
	private final String name;
	private final InetSocketAddress address;

	public Peer(String name, InetSocketAddress address) {
		this.name = name;
		this.address = address;

	}
	public InetSocketAddress getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}
}
