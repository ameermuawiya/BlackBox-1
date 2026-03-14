# Configures the Pine native library build.
# Sets up architecture-specific sources, compiler flags, and links libraries.
cmake_minimum_required(VERSION 3.10)
project(PineProject)

# Suppresses Ninja dirty manifest errors.
set(CMAKE_SUPPRESS_REGENERATION ON)

# Enables Assembly language support.
enable_language(ASM)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)

# Bsymbolic is strictly required for modern LLD to resolve Thumb-2 PC-relative
# relocations (R_ARM_THM_PC12) against global symbols in Assembly files.
set(LINKER_FLAGS "-ffixed-x18 -Wl,--hash-style=both -Wl,-Bsymbolic")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} ${LINKER_FLAGS}")
set(CMAKE_STATIC_LINKER_FLAGS "${CMAKE_STATIC_LINKER_FLAGS} ${LINKER_FLAGS}")
set(CMAKE_MODULE_LINKER_FLAGS "${CMAKE_MODULE_LINKER_FLAGS} ${LINKER_FLAGS}")
set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${LINKER_FLAGS}")

set(PINE_SOURCES
        pine.cpp
        ruler.cpp
        android.cpp
        jni_bridge.cpp
        art/art_method.cpp
        art/thread.cpp
        art/jit.cpp
        trampoline/trampoline_installer.cpp
        utils/memory.cpp
        utils/scoped_memory_access_protection.cpp
        utils/elf_img.cpp
        utils/well_known_classes.cpp)

if (CMAKE_ANDROID_ARCH_ABI STREQUAL "armeabi-v7a")
    set(CMAKE_ASM_FLAGS "${CMAKE_ASM_FLAGS} -arch armv7 -fPIC")
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -mno-movt")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mno-movt")
    
    set(PINE_SOURCES
            ${PINE_SOURCES}
            trampoline/arch/thumb2.cpp
            trampoline/arch/thumb2.S)

elseif(CMAKE_ANDROID_ARCH_ABI STREQUAL "arm64-v8a")
    set(CMAKE_ASM_FLAGS "${CMAKE_ASM_FLAGS} -arch arm64")
    set(PINE_SOURCES
            ${PINE_SOURCES}
            trampoline/arch/arm64.cpp
            trampoline/arch/arm64.S)
elseif(CMAKE_ANDROID_ARCH_ABI STREQUAL "x86")
    set(CMAKE_ASM_FLAGS "${CMAKE_ASM_FLAGS} -arch x86")
    set(PINE_SOURCES
            ${PINE_SOURCES}
            trampoline/arch/x86.cpp)
else()
    message(FATAL_ERROR "Unsupported cpu abi ${CMAKE_ANDROID_ARCH_ABI}")
endif()

add_library(pine SHARED ${PINE_SOURCES})

find_library(log-lib log)

target_link_libraries(pine ${log-lib})
