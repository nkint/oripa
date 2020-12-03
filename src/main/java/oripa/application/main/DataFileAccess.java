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
package oripa.application.main;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import oripa.doc.Doc;
import oripa.persistent.doc.DocDAO;
import oripa.persistent.doc.DocFilterSelector;
import oripa.persistent.doc.FileTypeKey;
import oripa.persistent.filetool.FileAccessSupportFilter;
import oripa.persistent.filetool.FileChooserCanceledException;
import oripa.persistent.filetool.FileVersionError;
import oripa.persistent.filetool.WrongDataFormatException;

/**
 * @author OUCHI Koji
 *
 */
public class DataFileAccess {
	private static DataFileAccess instance = null;

	private DataFileAccess() {

	}

	public static DataFileAccess get() {
		if (instance == null) {
			instance = new DataFileAccess();
		}

		return instance;
	}

	/**
	 * save the doc to given path and set the path to the doc.
	 *
	 * @param doc
	 * @param filePath
	 * @param fileType
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void saveProjectFile(final Doc doc, final String filePath, final FileTypeKey fileType)
			throws IOException, IllegalArgumentException {

		final DocDAO dao = new DocDAO();
		dao.save(doc, filePath, fileType);

		doc.setDataFilePath(filePath);
	}

	/**
	 *
	 * @param document
	 * @param directory
	 * @param fileName
	 * @param owner
	 * @param filters
	 * @return the path of saved file. Empty if the file choosing is canceled.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@SafeVarargs
	public final Optional<String> saveFile(final Doc document,
			final String directory, String fileName, final Component owner,
			final FileAccessSupportFilter<Doc>... filters)
			throws IOException, IllegalArgumentException {

		if (fileName.isEmpty()) {
			fileName = "newFile.opx";
		}
		File givenFile = new File(directory, fileName);

		var filePath = givenFile.getPath();

		try {
			final DocDAO dao = new DocDAO();
			String savedPath = dao.saveUsingGUI(document, filePath, owner, filters);
			return Optional.of(savedPath);
		} catch (FileChooserCanceledException e) {
			return Optional.empty();
		}
	}

	public void saveFileWithModelCheck(final Doc document,
			final FileAccessSupportFilter<Doc> filter, final Component owner)
			throws FileChooserCanceledException, IOException, IllegalArgumentException {
		final DocDAO dao = new DocDAO();
		dao.saveUsingGUIWithModelCheck(document, owner, filter);
	}

	/**
	 * if filePath is null, this method opens a dialog to select the target.
	 * otherwise, it tries to read data from the path.
	 *
	 * @param filePath
	 * @return the path of loaded file. Empty if the file choosing is canceled.
	 */
	public Optional<Doc> loadFile(final String filePath, final DocFilterSelector filterSelector,
			final String lastFilePath, final Component owner)
			throws FileVersionError, WrongDataFormatException, IOException {
		DocDAO dao = new DocDAO();

		try {
			if (filePath != null) {
				return Optional.of(dao.load(filePath));
			} else {
				return Optional.of(dao.loadUsingGUI(
						lastFilePath, filterSelector.getLoadables(),
						owner));
			}
		} catch (FileChooserCanceledException cancel) {
			return Optional.empty();
		}
	}
}