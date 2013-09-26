package oripa.paint.pbisec;

import oripa.paint.core.PaintContext;
import oripa.paint.core.PickingVertex;

public class SelectingFirstVertexForBisector extends PickingVertex{
	
	public SelectingFirstVertexForBisector(){
		super();
	}

	@Override
	public void onResult(PaintContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initialize() {
		setPreviousClass(this.getClass());
		setNextClass(SelectingSecondVertexForBisector.class);
		
//		System.out.println("SelectingFirstVertex.initialize() is called");
	}
	
}
