package com.dccoleman.game;

public class Sector implements Comparable<Sector> {
	public SectorType type;
	
	private final long sector_id;
	
	public Sector(long sector_id) {
		this.sector_id = sector_id;
	}
	
	public long getSectorID() {
		return this.sector_id;
	}

	@Override
	public int compareTo(Sector o) {
		if(this.sector_id == o.getSectorID()) {
			return 0;
		} else if(this.sector_id > o.getSectorID()) {
			return 1;
		} else {
			return -1;
		}
	}
	
	@Override
	public String toString() {
		return (sector_id + "," + "sector " + sector_id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (sector_id ^ (sector_id >>> 32));
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
		Sector other = (Sector) obj;
		if (sector_id != other.sector_id)
			return false;
		return true;
	}
	
}
