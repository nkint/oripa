/**
 * ORIPA - Origami Pattern Editor
 * Copyright (C) 2013-     ORIPA OSS Project  https://github.com/oripa/oripa
 * Copyright (C) 2005-2009 Jun Mitani         http://mitani.cs.tsukuba.ac.jp/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package oripa.gui.presenter.main;

import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oripa.application.main.OrigamiModelInteractiveBuilder;
import oripa.domain.creasepattern.CreasePattern;
import oripa.domain.cutmodel.CutModelOutlinesHolder;
import oripa.domain.fold.Folder;
import oripa.domain.fold.FolderFactory;
import oripa.domain.fold.foldability.FoldabilityChecker;
import oripa.domain.fold.halfedge.OrigamiModel;
import oripa.gui.presenter.creasepattern.ScreenUpdater;
import oripa.gui.view.estimation.EstimationResultFrameFactory;
import oripa.gui.view.model.ModelViewFrameFactory;
import oripa.gui.view.util.ChildFrameManager;
import oripa.gui.viewsetting.main.MainScreenSetting;

/**
 * @author OUCHI Koji
 *
 */
public class FoldedModelWindowOpener {
	private static final Logger logger = LoggerFactory.getLogger(FoldedModelWindowOpener.class);

	private final JComponent ownerView;
	private final ChildFrameManager childFrameManager;
	private final Supplier<Boolean> needCleaningUpDuplication;
	private final Runnable showCleaningUpMessage;
	private final Runnable showFailureMessage;
	private final Runnable showNoAnswerMessage;

	public FoldedModelWindowOpener(
			final JComponent ownerView,
			final ChildFrameManager childFrameManager,
			final Supplier<Boolean> needCleaningUpDuplication,
			final Runnable showCleaningUpMessage,
			final Runnable showFailureMessage,
			final Runnable showNoAnswerMessage) {
		this.ownerView = ownerView;
		this.childFrameManager = childFrameManager;
		this.needCleaningUpDuplication = needCleaningUpDuplication;
		this.showCleaningUpMessage = showCleaningUpMessage;
		this.showFailureMessage = showFailureMessage;
		this.showNoAnswerMessage = showNoAnswerMessage;
	}

	public void showFoldedModelWindows(
			final CreasePattern creasePattern,
			final CutModelOutlinesHolder cutOutlinesHolder,
			final MainScreenSetting mainScreenSetting,
			final boolean fullEstimation,
			final ScreenUpdater screenUpdater) {

		var folderFactory = new FolderFactory();
		Folder folder = folderFactory.create();

		OrigamiModel origamiModel = buildOrigamiModel(creasePattern);
		var checker = new FoldabilityChecker();

		if (!checker.testLocalFlatFoldability(origamiModel)) {
			folder.foldWithoutLineType(origamiModel);
		} else {
			var foldedModel = folder.fold(
					origamiModel, fullEstimation);
			final int foldableModelCount = foldedModel.getFoldablePatternCount();

			if (fullEstimation) {

				if (foldableModelCount == 0) {
					showNoAnswerMessage.run();
				} else if (foldableModelCount > 0) {
					logger.info("foldable layer layout is found.");

					EstimationResultFrameFactory resultFrameFactory = new EstimationResultFrameFactory(
							childFrameManager);
					JFrame frame = resultFrameFactory.createFrame(ownerView, foldedModel);
					frame.repaint();
					frame.setVisible(true);
				}
			}
		}

		ModelViewFrameFactory modelViewFactory = new ModelViewFrameFactory(
				mainScreenSetting,
				childFrameManager);
		JFrame modelView = modelViewFactory.createFrame(ownerView, origamiModel,
				cutOutlinesHolder, screenUpdater::updateScreen);

		modelView.repaint();
		modelView.setVisible(true);
	}

	/**
	 * try building the crease pattern and ask for additional measures to help
	 * clean it
	 *
	 * @return folded Origami model
	 */
	private OrigamiModel buildOrigamiModel(final CreasePattern creasePattern) {
		var builder = new OrigamiModelInteractiveBuilder();

		return builder.build(
				creasePattern,
				needCleaningUpDuplication,
				showCleaningUpMessage,
				showFailureMessage);
	}

}
