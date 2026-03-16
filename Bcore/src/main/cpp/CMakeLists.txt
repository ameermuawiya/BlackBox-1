# Configures the CMake build for the native library.
# It defines the project, sets source directories, and links system libraries.
cmake_minimum_required(VERSION 3.10)
project(ABridgeProject)

include_directories(JniHook)
include_directories(./)

aux_source_directory(./ SRC1)
aux_source_directory(Utils SRC2)
aux_source_directory(Hook SRC3)
aux_source_directory(JniHook SRC4)

add_compile_options(-w)

add_library(ABridge SHARED BoxCore.cpp ${SRC1} ${SRC2} ${SRC3} ${SRC4})

find_library(log-lib log)

target_link_libraries(ABridge ${log-lib} z)
