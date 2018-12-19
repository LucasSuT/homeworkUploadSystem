#define _GNU_SOURCE

#include <dlfcn.h>
#include <stdio.h>
#include <stdlib.h>


static FILE* (*_fopen)(const char *filename, const char *mode) = NULL;

FILE* fopen(const char *filename, const char *mode) {
    printf("Detected evil code\n");
    return NULL;
}

