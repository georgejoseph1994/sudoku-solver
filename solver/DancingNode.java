package solver;


/*
 * Class representing a dancing node 
 */
class DancingNode{
	
        DancingNode Left, Right, Up, Down;
        ColumnNode Column;
        int rowNumber;
        
        public DancingNode(){
            Left = Right = Up = Down = this;
        }

        public DancingNode(ColumnNode c){
            this();
            Column = c;
        }
        
        public DancingNode(ColumnNode c, int rowNumber){
            this();
            Column = c;
            this.rowNumber = rowNumber;
        }
        
        DancingNode connectDown(DancingNode dNode){
        	dNode.Down = this.Down;
        	dNode.Down.Up = dNode;
        	dNode.Up = this;
            this.Down = dNode;
            return dNode;
        }

        DancingNode connectRight(DancingNode dNode){
        	dNode.Right = this.Right;
        	dNode.Right.Left = dNode;
        	dNode.Left = this;
            this.Right = dNode;
            return dNode;
        }

        void disconnectLR(){
            this.Left.Right = this.Right;
            this.Right.Left = this.Left;
        }

        void reconnectLR(){
            this.Left.Right = this.Right.Left = this;
        }

        void disconnectUD(){
            this.Up.Down = this.Down;
            this.Down.Up = this.Up;

        }

        void reconnectUD(){
            this.Up.Down = this.Down.Up = this;
        }

       
    }