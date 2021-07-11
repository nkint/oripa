package oripa.domain.paint.bisector;

import oripa.domain.paint.PaintContext;
import oripa.domain.paint.core.PickingLine;
import oripa.util.Command;

public class SelectingLineForBisector extends PickingLine {

	@Override
	protected void initialize() {
		setPreviousClass(SelectingVertexForBisector.class);
		setNextClass(SelectingVertexForBisector.class);

	}

	@Override
	protected void undoAction(final PaintContext context) {
		context.popVertex();

	}

	@Override
	protected void onResult(final PaintContext context, final boolean doSpecial) {
		if (context.getLineCount() != 1 ||
				context.getVertexCount() != 3) {
			throw new IllegalStateException("wrong state: impossible line and vertex selection.");
		}

		Command command = new BisectorLineAdderCommand(context);
		command.execute();
	}

}
