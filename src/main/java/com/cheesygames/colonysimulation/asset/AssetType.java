package com.cheesygames.colonysimulation.asset;

import com.cheesygames.colonysimulation.asset.loader.behaviortree.BehaviorTreeAsset;
import com.cheesygames.colonysimulation.asset.loader.behaviortree.BehaviorTreeAssetKey;
import com.jme3.asset.*;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioKey;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * All supported asset formats by JME3. Some of them require extra libraries.
 */
public enum AssetType {

    TEXTURE("jpg", "png", "gif", "tga", "pfm", "hdr", "dds") {
        @Override
        public Class<TextureKey> getAssetKeyClass() {
            return TextureKey.class;
        }

        @Override
        public Class<Texture> getAssetTypeClass() {
            return Texture.class;
        }

        @Override
        public TextureKey createAssetKey(String path) {
            return new TextureKey(path, TEXTURE_FLIP_Y);
        }
    },
    // MaterialDef, which does not extends AssetKey... Also, it doesn't need to be loaded by the user.
    MATERIAL_DEFINITION(null, "j3md") {
    },
    MATERIAL("j3m") {
        @Override
        public Class<MaterialKey> getAssetKeyClass() {
            return MaterialKey.class;
        }

        @Override
        public Class<Material> getAssetTypeClass() {
            return Material.class;
        }

        @Override
        public MaterialKey createAssetKey(String path) {
            return new MaterialKey(path);
        }
    },
    MODEL("j3o", "obj", "mesh.xml", "scene", "blend", "3ds", "dae") {
        @Override
        public Class<ModelKey> getAssetKeyClass() {
            return ModelKey.class;
        }

        @Override
        public Class<Spatial> getAssetTypeClass() {
            return Spatial.class;
        }

        @Override
        public ModelKey createAssetKey(String path) {
            return new ModelKey(path);
        }
    },
    FILTER_POST_PROCESSOR("j3f") {
        @Override
        public Class<FilterKey> getAssetKeyClass() {
            return FilterKey.class;
        }

        @Override
        public Class<FilterPostProcessor> getAssetTypeClass() {
            return FilterPostProcessor.class;
        }

        @Override
        public FilterKey createAssetKey(String path) {
            return new FilterKey(path);
        }
    },
    VERTEX_SHADER(null, "vert") {
    },
    FRAGMENT_SHADER(null, "frag") {
    },
    AUDIO("ogg", "wav") {
        @Override
        public Class<AudioKey> getAssetKeyClass() {
            return AudioKey.class;
        }

        @Override
        public Class<AudioData> getAssetTypeClass() {
            return AudioData.class;
        }

        @Override
        public AudioKey createAssetKey(String path) {
            return new AudioKey(path);
        }
    },
    BITMAP_FONT("fnt") {
        @Override
        public Class<BitmapFont> getAssetTypeClass() {
            return BitmapFont.class;
        }
    },
    GUI("jpg", "png", "gif", "tga", "pfm", "hdr", "dds") {
        @Override
        public Class<TextureKey> getAssetKeyClass() {
            return TextureKey.class;
        }

        @Override
        public Class<Texture> getAssetTypeClass() {
            return Texture.class;
        }

        @Override
        public TextureKey createAssetKey(String path) {
            return new TextureKey(path, TEXTURE_FLIP_Y);
        }
    },
    BEHAVIOR("btree") {
        @Override
        public Class<BehaviorTreeAssetKey> getAssetKeyClass() {
            return BehaviorTreeAssetKey.class;
        }

        @Override
        public Class<BehaviorTreeAsset> getAssetTypeClass() {
            return BehaviorTreeAsset.class;
        }

        @Override
        public BehaviorTreeAssetKey createAssetKey(String path) {
            return new BehaviorTreeAssetKey(path);
        }
    };

    /**
     * If textures should be flipped on the Y axis. JMonkey flips textures by default, so to revert that we need to flip them again.
     */
    private static final boolean TEXTURE_FLIP_Y = false;

    protected final List<String> m_supportedFormats;

    AssetType(String... supportedFormats) {
        this.m_supportedFormats = new ArrayList<>(Arrays.asList(supportedFormats));

        ((ArrayList<String>) m_supportedFormats).trimToSize();
    }

    /**
     * Creates an {@link AssetKey#} depending on the asset key class of the asset type.
     *
     * @param path The asset's path.
     *
     * @return A nbewly created {@link AssetKey#}.
     */
    @SuppressWarnings("rawtypes")
    public <T extends AssetKey> T createAssetKey(String path) {
        //noinspection unchecked
        return (T) new AssetKey(path);
    }

    public List<String> getSupportedFormats() {
        return m_supportedFormats;
    }

    @SuppressWarnings("rawtypes")
    public Class<? extends AssetKey> getAssetKeyClass() {
        return AssetKey.class;
    }

    public Class<?> getAssetTypeClass() {
        return Object.class;
    }
}
