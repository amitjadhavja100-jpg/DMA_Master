package com.icici.dma.slabEntity;

import java.util.List;

public class RowData {

	public String collection;
	public List<CellData> values;
	
		public String getCollection() {
			return collection;
		}
		public void setCollection(String collection) {
			this.collection = collection;
		}
		public List<CellData> getValues() {
			return values;
		}
		public void setValues(List<CellData> values) {
			this.values = values;
		}
	    
	    
}
