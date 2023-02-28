package net.finmath.xva;

import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * A surface is a collection of RandomVariables. It is supposed to store a
 * certain quantity over multiple times and over different omegas/realizations.
 */
public class Surface {

	private final FactorKey factorKey;
	private final String discountCurveName;
	private final RandomVariable[] dataFrame;
	private final String importPath;
	private final TimeDiscretization timeDiscretization; // column header

	/**
	 * Construct a surface.
	 * 
	 * @param factorId           e.g. a trade ID
	 * @param factorType         e.g.
	 * @param metric             metric in the surface
	 * @param discountCurveName  name of the discount curve
	 * @param dataFrame          the matrix with the data.
	 * @param importPath         import a surface from a file.
	 * @param timeDiscretization the time discretization of the simulation.
	 */
	public Surface(String factorId, FactorType factorType, Metric metric, String discountCurveName,
			RandomVariable[] dataFrame, String importPath, TimeDiscretization timeDiscretization) {
		super();
		this.factorKey = new FactorKey(factorId, factorType, metric);
		this.discountCurveName = discountCurveName;
		this.dataFrame = dataFrame;
		this.importPath = importPath;
		this.timeDiscretization = timeDiscretization;
	}

	public Surface(String factorId, FactorType factorType, Metric metric, String discountCurveName,
			RandomVariable[] dataFrame, TimeDiscretization timeDiscretization) {
		super();
		this.factorKey = new FactorKey(factorId, factorType, metric);
		this.discountCurveName = discountCurveName;
		this.dataFrame = dataFrame;
		this.importPath = null;
		this.timeDiscretization = timeDiscretization;
	}

	public Surface(String tradeId, FactorType factorType, Metric metric, String discountCurveName, String importPath,
			TimeDiscretization timeDiscretization) {
		super();
		this.factorKey = new FactorKey(tradeId, factorType, metric);
		this.discountCurveName = discountCurveName;
		this.dataFrame = fillWithValuesFrom(importPath);
		this.importPath = importPath;
		this.timeDiscretization = timeDiscretization;
	}

	/**
	 * Returns the column corresponding to a certain time instant.
	 * 
	 * @param time a valid point in time.
	 * @return the corresponding column.
	 */
	public RandomVariable getColumn(double time) {
		int index = timeDiscretization.getTimeIndex(time);
		return dataFrame[index];
	}

	/**
	 * Returns the column corresponding to a certain time index.
	 * 
	 * @param index must be valid for the time discretization.
	 * @return the corresponding column.
	 */
	public RandomVariable getColumn(int index) {
		return dataFrame[index];
	}

	/**
	 * Gets the factorKey.
	 * 
	 * @return the factorKey
	 */
	public FactorKey getFactorKey() {
		return this.factorKey;
	}

	/**
	 * Gets the tradeId.
	 * 
	 * @return the tradeId
	 */
	public String getTradeId() {
		return this.factorKey.getFactorId();
	}

	/**
	 * Gets the factorType.
	 * 
	 * @return the factorType
	 */
	public FactorType getFactorType() {
		return this.factorKey.getFactorType();
	}

	/**
	 * Gets the metric.
	 * 
	 * @return the metric
	 */
	public Metric getMetric() {
		return this.factorKey.getMetric();
	}

	/**
	 * Gets the discountCurveName.
	 * 
	 * @return the discountCurveName
	 */
	public String getDiscountCurveName() {
		return discountCurveName;
	}

	/**
	 * Gets the dataFrame.
	 * 
	 * @return the dataFrame
	 */
	public RandomVariable[] getDataFrame() {
		return dataFrame;
	}

	/**
	 * Gets the importPath.
	 * 
	 * @return the importPath
	 */
	public String getImportPath() {
		return importPath;
	}

	/**
	 * Gets the timeDiscretization.
	 * 
	 * @return the timeDiscretization
	 */
	public TimeDiscretization getTimeDiscretization() {
		return timeDiscretization;
	}

	private RandomVariable[] fillWithValuesFrom(String importPath) {
		// Read data from csv or Excel, each column is a new RandomVariable
		// Many checks to to.
		throw new UnsupportedOperationException("Not implemented, yet");
	}

	public void exportSurfaceToExcel() {
		// TODO: use Apache POI to export the surface on an Excel file.
		// The name of the file is given by the factor key
		// https://poi.apache.org/index.html
		throw new UnsupportedOperationException("Not implemented, yet");
	}

}
