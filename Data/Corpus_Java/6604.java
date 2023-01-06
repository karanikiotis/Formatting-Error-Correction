package com.vladsch.flexmark.ast;

import com.vladsch.flexmark.util.sequence.BasedSequence;

import java.util.List;

/**
 * HTML block
 *
 * @see <a href="http://spec.commonmark.org/0.18/#html-blocks">CommonMark Spec</a>
 */
public class HtmlBlock extends HtmlBlockBase {
    @Override
    public BasedSequence[] getSegments() {
        return EMPTY_SEGMENTS;
    }

    public HtmlBlock() {
    }

    public HtmlBlock(BasedSequence chars) {
        super(chars);
    }

    public HtmlBlock(BasedSequence chars, List<BasedSequence> segments) {
        super(chars, segments);
    }

    public HtmlBlock(BlockContent blockContent) {
        super(blockContent);
    }
}
