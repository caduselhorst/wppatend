package br.com.wppatend.vos;

public class AlterarSenhaVO {
	
	private Long idUsuario;
	private String newPassword;
	private String confirmPassword;
	private String erro;
	
	public AlterarSenhaVO() {}
	
	public AlterarSenhaVO(Long idUsuario, String newPassword, String confirmPassword, String erro) {
		super();
		this.idUsuario = idUsuario;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
		this.erro = erro;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}
	
}
