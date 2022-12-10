package br.com.wppatend.wpprequest.model;

public class SenderName {
	
	private String jid;
	private String name;
	private String vname;
	private String verify;
	//private String short;
	private String notify;
	private String dm_duration;
	private String dm_t;
	
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	public String getNotify() {
		return notify;
	}
	public void setNotify(String notify) {
		this.notify = notify;
	}
	public String getDm_duration() {
		return dm_duration;
	}
	public void setDm_duration(String dm_duration) {
		this.dm_duration = dm_duration;
	}
	public String getDm_t() {
		return dm_t;
	}
	public void setDm_t(String dm_t) {
		this.dm_t = dm_t;
	}
	

}
