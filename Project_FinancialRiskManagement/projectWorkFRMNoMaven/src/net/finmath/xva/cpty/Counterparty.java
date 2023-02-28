package net.finmath.xva.cpty;

/**
 * A minimal class representing a cpty.
 * A counterparty has a rating plus a HashMap for additional information.
 * 
 * @author Alessandro Gnoatto
 */
import java.util.HashMap;

public class Counterparty {
	private final String cptyId;
	private final String name;
	private final String rating;
	private final HashMap<String, Object> additionalCptyInfo;

	public Counterparty(String counterpartyId, String name, String rating) {
		this.cptyId = counterpartyId;
		this.name = name;
		this.rating = rating;
		this.additionalCptyInfo = new HashMap<String, Object>();
	}

	public Counterparty(String counterpartyId, String name, String rating, HashMap<String, Object> additionalCptyInfo) {
		this.cptyId = counterpartyId;
		this.name = name;
		this.rating = rating;
		this.additionalCptyInfo = additionalCptyInfo;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getCptyId() {
		return cptyId;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the rating.
	 * 
	 * @return the rating
	 */
	public String getRating() {
		return rating;
	}

	/**
	 * Gets the additionalCptyInfo.
	 * 
	 * @return the additionalCptyInfo
	 */
	public HashMap<String, Object> getAdditionalCptyInfo() {
		return additionalCptyInfo;
	}

	public Counterparty getCloneForModifiedRating(String newRating) {
		return new Counterparty(this.cptyId, this.name, newRating, this.additionalCptyInfo);
	}

	public Counterparty getCloneForModifiedAdditionalData(String field, Object value) {
		/*
		 * The user should make sure that the field exists, otherwise this method will
		 * enlarge additional info.
		 */
		additionalCptyInfo.put(field, value);
		return new Counterparty(this.cptyId, this.name, this.rating, this.getAdditionalCptyInfo());
	}

}
