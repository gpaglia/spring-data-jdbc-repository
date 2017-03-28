package cz.jirutka.spring.data.jdbc.ext.fixtures;

import java.util.Date;

public class EUser {
    private String userName;
    private Date dateOfBirth;
    private int reputation;
    private boolean enabled;


    public EUser(String userName, Date dateOfBirth, int reputation, boolean enabled) {
        this.userName = userName;
        this.dateOfBirth = dateOfBirth;
        this.reputation = reputation;
        this.enabled = enabled;
    }


	public String getUserName() {
		return userName;
	}


	public Date getDateOfBirth() {
		return dateOfBirth;
	}


	public int getReputation() {
		return reputation;
	}


	public boolean isEnabled() {
		return enabled;
	}


}
