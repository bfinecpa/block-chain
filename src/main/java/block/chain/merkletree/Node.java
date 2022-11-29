package block.chain.merkletree;

import java.io.Serializable;

public interface Node extends Serializable {

    String getHash();

    Node getLeft();

    Node getRight();
}
