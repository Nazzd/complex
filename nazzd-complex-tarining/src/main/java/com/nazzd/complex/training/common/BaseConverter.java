package com.nazzd.complex.training.common;

import java.util.List;

/**
 * Contract for a generic dto to po converter.
 *
 * @param <B> - BO.
 * @param <P> - PO.
 */
public interface BaseConverter<B, P> {

    P toPo(B bo);

    B toBo(P po);

    List<P> toPo(List<B> boList);

    List<B> toBo(List<P> poList);

}

