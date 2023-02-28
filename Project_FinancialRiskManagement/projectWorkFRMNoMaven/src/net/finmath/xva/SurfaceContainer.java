package net.finmath.xva;

import java.util.HashMap;

import net.finmath.time.TimeDiscretization;

/**
 * This class is meant to contain the Mark-to-market cube. We store here the
 * value of - Every contingent claim - For every point in time - For every
 * possible state.
 * 
 * This is a three dimensional structure, hence the name Mark-to-market cube.
 * 
 * All surfaces should have the same number of columns, otherwise this will
 * create a time inconsistency.
 * 
 * The class serves however also as general purpose container of data for risk
 * reports.
 */
public class SurfaceContainer {
	private final HashMap<FactorKey, Surface> container;
	/*
	 * Common time discretization for all trades in the netting set.
	 */
	private final TimeDiscretization timeDiscretization;
	/*
	 * The base curve of the hybrid model
	 */
	private final String discountCurveName;

	public SurfaceContainer(TimeDiscretization timeDiscretization, String discountCurveName) {
		this.container = new HashMap<FactorKey, Surface>();
		this.timeDiscretization = timeDiscretization;
		this.discountCurveName = discountCurveName;
	}

	public SurfaceContainer(HashMap<FactorKey, Surface> container, TimeDiscretization timeDiscretization,
			String discountCurveName) {
		this.container = container;
		this.timeDiscretization = timeDiscretization;
		this.discountCurveName = discountCurveName;
	}

	/**
	 * Add a surface to the surface container. The method returns a clone of the
	 * surface container where the map contains the newly inserted surface.
	 * 
	 * @param surface the surface to add
	 * @return an extended clone of the surface
	 */
	public SurfaceContainer addSurface(Surface surface) {
		/*
		 * HashMap<FactorKey, Surface> newContainer = new HashMap<FactorKey, Surface>();
		 * 
		 * for(Entry<FactorKey, Surface> entry : container.entrySet()) { FactorKey key =
		 * entry.getKey(); Surface value = entry.getValue(); newContainer.put(key,
		 * value); }
		 * 
		 * FactorKey factorKey = surface.getFactorKey(); newContainer.put(factorKey,
		 * surface);
		 */
		FactorKey factorKey = surface.getFactorKey();
		container.put(factorKey, surface);
		return getCloneForModifiedContainer(container);
	}

	public Surface getSurface(FactorKey factorKey) {
		if (this.hasFactor(factorKey)) {
			return this.container.get(factorKey);
		} else {
			throw new IllegalArgumentException("Missing " + factorKey.toString() + " in the surface container.");
		}
	}

	/**
	 * Returns a clone of the container with a modified data structure.
	 * 
	 * @param container the new surface container.
	 * @return returns a clone with the changed container.
	 */
	public SurfaceContainer getCloneForModifiedContainer(HashMap<FactorKey, Surface> container) {
		return new SurfaceContainer(container, this.timeDiscretization, this.discountCurveName);
	}

	public boolean hasFactor(FactorKey factorKey) {
		return container.containsKey(factorKey);
	}

	/**
	 * Gets the container.
	 * 
	 * @return the container
	 */
	public HashMap<FactorKey, Surface> getContainer() {
		return container;
	}

	/**
	 * Gets the timeDiscretization.
	 * 
	 * @return the timeDiscretization
	 */
	public TimeDiscretization getTimeDiscretization() {
		return timeDiscretization;
	}

	/**
	 * Gets the discountCurveName.
	 * 
	 * @return the discountCurveName
	 */
	public String getDiscountCurveName() {
		return discountCurveName;
	}

}
