#define _GNU_SOURCE

#include <dlfcn.h>
#include <stdio.h>
#include <stdlib.h>

static int (*_system)(const char *cmd) = NULL;

int system(const char *cmd) {
    printf("Detected evil code\n");
    return 0;
}

