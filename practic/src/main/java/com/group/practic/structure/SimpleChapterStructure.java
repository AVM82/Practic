package com.group.practic.structure;

import java.util.ArrayList;
import java.util.List;

public class SimpleChapterStructure {

	int number;
	String header;
	List<SimpleChapterStructure> offsprings = new ArrayList<>();

	public SimpleChapterStructure() {
	}

	public SimpleChapterStructure(int number, String header) {
		this.number = number;
		this.header = header;
	}

	public void newOffspring(SimpleChapterStructure offspring) {
		offsprings.add(offspring);
	}

	public List<SimpleChapterStructure> getOffsprings() {
		return offsprings;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void sortOffsprings() {
		if (offsprings != null && offsprings.size() > 1) {
			int current = 0;
			int min = offsprings.get(current).getNumber();
			for (int i = 1; i < offsprings.size(); i++) {
				if (min > offsprings.get(i).getNumber()) {
					SimpleChapterStructure x = offsprings.get(current);
					offsprings.set(current, offsprings.get(i));
					offsprings.set(i, x);
					min = offsprings.get(current).getNumber();
				}
			}
		}
	}

}
