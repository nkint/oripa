package oripa.domain.paint.deletevertex;

import java.awt.geom.AffineTransform;

import oripa.domain.paint.EditMode;
import oripa.domain.paint.ObjectGraphicDrawer;
import oripa.domain.paint.PaintContextInterface;
import oripa.domain.paint.core.GraphicMouseAction;

public class DeleteVertexAction extends GraphicMouseAction {

	public DeleteVertexAction() {
		setEditMode(EditMode.VERTEX);

		setActionState(new DeletingVertex());

	}

	@Override
	public void onDrag(final PaintContextInterface context, final boolean differentAction) {

	}

	@Override
	public void onRelease(final PaintContextInterface context, final AffineTransform affine,
			final boolean differentAction) {

	}

	@Override
	public void onDraw(final ObjectGraphicDrawer drawer, final PaintContextInterface context) {

		super.onDraw(drawer, context);

		drawPickCandidateVertex(drawer, context);
	}

	@Override
	public void onPress(final PaintContextInterface context, final boolean differentAction) {
	}

}
