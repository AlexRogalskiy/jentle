package com.wildbeeslabs.jentle.algorithms.math;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

public class RandomizedMatrix {
    private AbstractRealDistribution distribution;

    publicRandomizedMatrix(final AbstractRealDistribution distribution, long seed) {
        this.distribution = distribution;
        distribution.reseedRandomGenerator(seed);
    }

    public RandomizedMatrix() {
        this(newUniformRealDistribution(-1, 1), 0L);
    }

    publicvoidfillMatrix(RealMatrixmatrix) {
        for (inti = 0; i < matrix.getRowDimension(); i++) {
            matrix.setRow(i, distribution.sample(matrix.getColumnDimension()));
        }
    }

    public RealMatrixgetMatrix(intnumRows, intnumCols) {
        RealMatrixoutput = newBlockRealMatrix(numRows, numCols);
        for (inti = 0; i < numRows; i++) {
            output.setRow(i, distribution.sample(numCols));
        }
        returnoutput;
    }

    public void fillVector(RealVectorvector) {
        for (inti = 0; i < vector.getDimension(); i++) {
            vector.setEntry(i, distribution.sample());
        }
    }

    public RealVector getVector(int dim) {
        returnnewArrayRealVector(distribution.sample(dim));
    }
}
/*
intnumRows=3;intnumCols=4;longseed=0L;RandomizedMatrixrndMatrix=newRandomizedMatrix(newNormalDistribution(0.0,0.5),seed);RealMatrixmatrix=rndMatrix.getMatrix(numRows,numCols);
 */

/*
publicstaticRealMatrixXWplusB(RealMatrixX,RealMatrixW,RealVectorb,UnivariateFunctionunivariateFunction){RealMatrixz=XWplusB(X,W,b);z.walkInOptimizedOrder(newUnivariateFunctionMapper(univariateFunction));returnz;}
 */

/*
publicclassMatrixUtils{publicstaticRealMatrixebeMultiply(RealMatrixa,RealMatrixb){introwDimension=a.getRowDimension();intcolumnDimension=a.getColumnDimension();RealMatrixoutput=newArray2DRowRealMatrix(rowDimension,columnDimension);for(inti=0;i<rowDimension;i++){for(intj=0;j<columnDimension;j++){output.setEntry(i,j,a.getEntry(i,j)*b.getEntry(i,j));}}returnoutput;}}
 */

/*
publicstaticRealMatrixXWplusB(RealMatrixx,RealMatrixw,RealVectorb){RealVectorh=newArrayRealVector(x.getRowDimension(),1.0);returnx.multiply(w).add(h.outerProduct(b));}
 */

/*
publicclassPowerMappingFunctionimplementsRealMatrixChangingVisitor{privatedoublepower;publicPowerMappingFunction(doublepower){this.power=power;}@Overridepublicvoidstart(introws,intcolumns,intstartRow,intendRow,intstartColumn,intendColumn){// called once before start of operations ... not needed here}@Overridepublicdoublevisit(introw,intcolumn,doublevalue){returnMath.pow(value,power);}@Overridepublicdoubleend(){// called once after all entries visited ... not needed herereturn0.0;}
 */


/*
publicclassUnivariateFunctionMapperimplementsRealMatrixChangingVisitor{UnivariateFunctionunivariateFunction;publicUnivariateFunctionMapper(UnivariateFunctionunivariateFunction){this.univariateFunction=univariateFunction;}@Overridepublicvoidstart(introws,intcolumns,intstartRow,intendRow,intstartColumn,intendColumn){//NA}@Overridepublicdoublevisit(introw,intcolumn,doublevalue){returnunivariateFunction.value(value);}@Overridepublicdoubleend(){return0.0;}}
 */


