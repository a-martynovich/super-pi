package net.bsayiner.SuperPi.Utilies;

import java.io.Serializable;

public class CalculationRecords implements Serializable {

	private static final long serialVersionUID = -7760592622114437210L;

	private String[] records = new String[9];

	public void addRecord(int index, String record) {
		this.records[index] = record;
	}

}
