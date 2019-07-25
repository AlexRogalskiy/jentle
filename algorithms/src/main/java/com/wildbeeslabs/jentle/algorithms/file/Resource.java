package com.wildbeeslabs.jentle.algorithms.file;

import java.io.IOException;
import java.io.InputStream;

public abstract class Resource {
    private final ResourceType resourceType;
    private InputStream stream;

    protected Resource(final String resourceType) {
        this.resourceType = this.identifyResourceType(resourceType);
    }

    protected Resource(final ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public abstract boolean canBeOpened();

    protected abstract InputStream openStream()
        throws IOException;

    public InputStream getInputStream()
        throws IOException {
        if (stream == null) {
            stream = openStream();
        }

        return stream;
    }

    protected ResourceType identifyResourceType(final String fileName) throws IllegalArgumentException {
        final ResourceType resourceType = ResourceType.getByFileName(fileName);
        if (resourceType == null) {
            throw new IllegalArgumentException("Cannot find a resource type for " + fileName);
        }

        return resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
