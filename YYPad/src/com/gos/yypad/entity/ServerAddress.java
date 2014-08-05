package com.gos.yypad.entity;

public class ServerAddress implements ApplicationEntity {
	private String serverAddressString;//服务器地址

	public String getServerAddressString() {
		return serverAddressString;
	}

	public void setServerAddressString(String serverAddressString) {
		this.serverAddressString = serverAddressString;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serverAddressString == null) ? 0 : serverAddressString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerAddress other = (ServerAddress) obj;
		if (serverAddressString == null) {
			if (other.serverAddressString != null)
				return false;
		} else if (!serverAddressString.equals(other.serverAddressString))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "ServerAddress [server_address=" + serverAddressString + "]";
	}
}
