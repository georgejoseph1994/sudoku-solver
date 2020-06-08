package solver;

class ColumnNode extends DancingNode {
	int size;
	String name;

	public ColumnNode(String n) {
		super();
		size = 0;
		name = n;
		Column = this;
	}

	/* Covering operation */
	void cover() {
		disconnectLR();
		for (DancingNode i = this.Down; i != this; i = i.Down) {
			for (DancingNode j = i.Right; j != i; j = j.Right) {
				j.disconnectUD();
				j.Column.size--;
			}
		}
	}

	/* uncovering operation */
	void uncover() {
		reconnectLR();
		for (DancingNode i = this.Up; i != this; i = i.Up) {
			for (DancingNode j = i.Left; j != i; j = j.Left) {
				j.Column.size++;
				j.reconnectUD();
			}
		}
		
	}
}
