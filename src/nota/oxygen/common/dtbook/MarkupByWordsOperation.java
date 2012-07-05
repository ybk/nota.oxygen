package nota.oxygen.common.dtbook;

import nota.oxygen.common.BaseAuthorOperation;
import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.node.AuthorNode;

/**
 * @author OHA
 *
 */
public class MarkupByWordsOperation extends BaseAuthorOperation {
	
	
	@Override
	protected void doOperation() throws AuthorOperationException {
		markupPositives();
	}
	
	private int[] findNextOccurance(String[] words) throws AuthorOperationException
	{
		AuthorDocumentController docCtrl = getAuthorAccess().getDocumentController();
		if (words.length>0)
		{
			String xpathStatement = words[0];
			for (int i = 1; i<words.length; i++) xpathStatement += "|"+words[i];
			AuthorNode[] candidates = docCtrl.findNodesByXPath(xpathStatement, false, true, true);
			int startIndex = -1;
			int currentCaretPos = getAuthorAccess().getEditorAccess().getCaretOffset();
			for (int i=0; i<candidates.length; i++) {
				if (currentCaretPos<=candidates[i].getStartOffset()) {
					startIndex = i;
					break;
				}
			}
			if (startIndex==-1) startIndex = 0;
			for (int i=startIndex; i<candidates.length; i++)
			{
				int[] res = findNextInAuthorNode(candidates[i], words);
				if (res.length==2) return res;
			}
			for (int i=0; i<startIndex; i++)
			{
				int[] res = findNextInAuthorNode(candidates[i], words);
				if (res.length==2) return res;
			}
		}
		return new int[] {};
	}
	
	private int[] findNextInAuthorNode(AuthorNode node, String[] words)
	{
		return new int[] {};
	}
	
	private void markupWord(int startOffset, int endOffset)
	{
		
		//AuthorDocumentController docCtrl = getAuthorAccess().getDocumentController();
		
	}
	
	
	private void markupPositives() throws AuthorOperationException
	{
		String[] words = getWords(WordTypes.Positive);
		int res[] = findNextOccurance(words);
		while (res.length==2)
		{
			markupWord(res[0], res[1]);
			res = findNextOccurance(words);
		}
	}
	
	private static String ARG_MARKUP = "markup fragment";
	private String markup;
	
	private static String ARG_WORD_FILE = "word file";
	private String wordFile;
	
	private enum WordTypes
	{
		Positive,
		Possible
	}
	
	private String[] getWords(WordTypes type)
	{
		switch (type)
		{
		case Positive:
			return new String[] {"NATO", "FN", "EU", "USA"};
		case Possible:
			return new String[] {"SMK", "LG", "HTC"};
		default:
			return new String[] {};
		}
	}
	

	@Override
	public ArgumentDescriptor[] getArguments() {
		return new ArgumentDescriptor[] {
				new ArgumentDescriptor(ARG_MARKUP, ArgumentDescriptor.TYPE_FRAGMENT, "Markup fragment - $word is placeholder for the word"),
				new ArgumentDescriptor(ARG_WORD_FILE, ArgumentDescriptor.TYPE_FRAGMENT, "Word file")
		};
	}
 
	@Override
	public String getDescription() {
		return "Markup by words";
	}

	@Override
	protected void parseArguments(ArgumentsMap args)
			throws IllegalArgumentException {
		markup = (String)args.getArgumentValue(ARG_MARKUP);
		wordFile = (String)args.getArgumentValue(ARG_WORD_FILE);
	}

}