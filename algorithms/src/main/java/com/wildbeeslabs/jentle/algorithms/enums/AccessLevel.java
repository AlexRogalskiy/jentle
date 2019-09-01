//package com.wildbeeslabs.jentle.algorithms.enums;
//
//import com.strobel.assembler.metadata.FieldDefinition;
//import com.strobel.assembler.metadata.Flags;
//import com.strobel.assembler.metadata.MethodDefinition;
//import com.strobel.assembler.metadata.TypeDefinition;
//
///**
// * @author shustkost
// *
// */
//public enum AccessLevel {
//    PUBLIC, PROTECTED, PACKAGE, PRIVATE;
//
//    public AccessLevel min(AccessLevel other) {
//        return other.ordinal() > ordinal() ? other : this;
//    }
//
//    public int select(int publicPriority, int protectedPriority, int defaultPriority, int privatePriority) {
//        switch(this) {
//        case PUBLIC:
//            return publicPriority;
//        case PROTECTED:
//            return protectedPriority;
//        case PACKAGE:
//            return defaultPriority;
//        case PRIVATE:
//            return privatePriority;
//        default:
//            throw new InternalError();
//        }
//    }
//
//    public static AccessLevel ofFlags(long flags) {
//        if (Flags.testAny(flags, Flags.PUBLIC))
//            return PUBLIC;
//        if (Flags.testAny(flags, Flags.PROTECTED))
//            return PROTECTED;
//        if (Flags.testAny(flags, Flags.PRIVATE))
//            return PRIVATE;
//        return PACKAGE;
//    }
//
//    public static AccessLevel of(MethodDefinition md) {
//        TypeDefinition td = md.getDeclaringType();
//        return ofFlags(td.getFlags()).min(ofFlags(md.getFlags()));
//    }
//
//    public static AccessLevel of(FieldDefinition fd) {
//        TypeDefinition td = fd.getDeclaringType();
//        return ofFlags(td.getFlags()).min(ofFlags(fd.getFlags()));
//    }
//}
