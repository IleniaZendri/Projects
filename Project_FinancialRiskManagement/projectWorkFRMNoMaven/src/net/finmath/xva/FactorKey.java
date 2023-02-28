package net.finmath.xva;

public class FactorKey {
	private final String factorId;
	private final FactorType factorType;
	private final Metric metric;

	/**
	 * Generate a factor key.
	 * 
	 * @param factorId   e.g. "999999"
	 * @param factorType e.g.
	 * @param metric     e.g. Metric.Value
	 */
	public FactorKey(String factorId, FactorType factorType, Metric metric) {
		super();
		this.factorId = factorId;
		this.factorType = factorType;
		this.metric = metric;
	}

	/**
	 * Gets the factorId.
	 * 
	 * @return the factorId
	 */
	public String getFactorId() {
		return factorId;
	}

	/**
	 * Gets the factorType.
	 * 
	 * @return the factorType
	 */
	public FactorType getFactorType() {
		return factorType;
	}

	/**
	 * Gets the metric.
	 * 
	 * @return the metric
	 */
	public Metric getMetric() {
		return metric;
	}

	@Override
	public String toString() {
		return "FactorKey [factorId=" + factorId + ", factorType=" + factorType + ", metric=" + metric + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((factorId == null) ? 0 : factorId.hashCode());
		result = prime * result + ((factorType == null) ? 0 : factorType.hashCode());
		result = prime * result + ((metric == null) ? 0 : metric.hashCode());
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
		FactorKey other = (FactorKey) obj;
		if (factorId == null) {
			if (other.factorId != null)
				return false;
		} else if (!factorId.equals(other.factorId))
			return false;
		if (factorType != other.factorType)
			return false;
		if (metric != other.metric)
			return false;
		return true;
	}

}
