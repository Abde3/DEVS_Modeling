package NOC;


import DEVSModel.DEVSModel;
import NocTopology.NOCDirections.ICoordinateSystem;
import NocTopology.NOCDirections.IDirection;

import java.util.Collection;
import java.util.HashMap;


public abstract class INocNetwork implements Iterable<DEVSModel>  {

    protected HashMap<ICoordinateSystem, DEVSModel> units;    /***** Represent all the units in the model ***************/


    abstract public Collection<DEVSModel> getAllUnits();
    abstract public DEVSModel   getUnitAt( ICoordinateSystem position );
    abstract public boolean     addUnitAt( DEVSModel model, ICoordinateSystem position );
    abstract public Collection<ICoordinateSystem> getAllPositions();
    abstract public DEVSModel   getUnitFromPosition( ICoordinateSystem position, IDirection direction );

}
