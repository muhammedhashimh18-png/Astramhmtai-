package com.example.data.engine

import kotlin.random.Random

object CosmicPrompts {

    val AUTO_IMAGE_PRESETS = listOf(
        "Hyper-dimensional cybernetic nebula citadel with glowing cyan plasma rings and crystalline towers orbiting a pulsar star, 8k resolution, cinematic lighting",
        "Bioluminescent astral sentinel meditating inside a cosmic tachyon garden with vibrant magenta aurora streams and floating sacred geometry",
        "Deep space exploration vessel entering a glowing quantum wormhole surrounded by chromatic refraction and stardust particle trails",
        "Futuristic holographic neural core humming with cyan and magenta fiber-optic energy pulses in an obsidian command bridge",
        "Cosmic planetary observatory perched upon an icy asteroid ring overlooking an ethereal binary sun supernova"
    )

    val AUTO_VIDEO_PRESETS = listOf(
        VideoBlueprint(
            title = "Quantum Gateway Warp Horizon",
            cameraMotion = "Dolly forward accelerating through cyan vortex rings with 360-degree rotational drift",
            framerate = "60 FPS Cinematic",
            aspectRatio = "16:9 Landscape",
            lighting = "Bioluminescent cyan and pulsing neon magenta volumetric fog",
            scenePrompt = "A sleek cosmic vessel engages its Alcubierre warp drive, streaking into a kaleidoscopic light corridor that bends space-time",
            duration = "8s Slow-Motion Loop"
        ),
        VideoBlueprint(
            title = "Neural Astral Genesis",
            cameraMotion = "Smooth orbiting 3D camera pan around floating crystalline matrix",
            framerate = "24 FPS Film Standard",
            aspectRatio = "9:16 Portrait Reel",
            lighting = "High-contrast ultraviolet with rim-lit starlight",
            scenePrompt = "Synapses of an artificial sentient nebula interconnecting, sending shimmering pulses across galaxies",
            duration = "10s Cinematic"
        ),
        VideoBlueprint(
            title = "Cybernetic City of Sector 9",
            cameraMotion = "FPV drone descent between towering holographic skyscrapers down to neon cyber-canals",
            framerate = "60 FPS Ultra Smooth",
            aspectRatio = "16:9 Landscape",
            lighting = "Reflective wet pavement with glowing neon cyan and magenta signage",
            scenePrompt = "Hovering transit pods weaving through rain-slicked mega-towers under a planetary eclipse",
            duration = "12s Extended"
        )
    )

    val ANIMATION_STYLES = listOf(
        "2D Cel-Shaded Cosmic Anime (Ufotable style)",
        "3D Hyper-Real Unreal Engine 5 VFX",
        "Holographic Cyber-Glitch Vector",
        "Stop-Motion Claymation Sci-Fi",
        "Retro Pixel-Art 16-Bit Nebula"
    )

    val AUTO_STORY_PRESETS = listOf(
        StoryChapter(
            chapterTitle = "Chapter 1: The Tachyon Beacon",
            setting = "Edge of the Oort Cloud, Deep Sector Zeta",
            narrative = "Commander Lyra pulled back the throttle of the Astral Horizon as sensor readouts spiked into the crimson spectrum. A pulse—structured, rhythmic, and undeniably conscious—was radiating from inside the shattered core of an ancient metallic comet.",
            choices = listOf(
                "Deploy a quantum probe into the comet's fissure",
                "Broadcast an encrypted ASTRAM diplomatic greeting code",
                "Charge defensive shields and scan for cloaked signatures"
            )
        ),
        StoryChapter(
            chapterTitle = "Chapter 1: The Neon Synthetics of Helios Prime",
            setting = "Cyber-Metropolis Upper Tier, Helios Orbit",
            narrative = "In the year 2389, the line between synthetic souls and organic memory ceased to exist. Jax adjusted his cybernetic retinal implant as a fragmented encrypted message flashed across his HUD: 'They are unravelling the prime sequence.'",
            choices = listOf(
                "Infiltrate the Central Neural Core mainframe",
                "Meet the enigmatic informant at the Nebula Club",
                "Wipe current memory cache and escape off-world"
            )
        )
    )

    val AUTO_MOVIE_PRESETS = listOf(
        MovieScene(
            sceneNumber = "SCENE 01",
            heading = "EXT. KINETIC ORBITAL DOCK - NIGHT (DEEP SPACE)",
            visualVFX = "Volumetric cyan starlight glinting off carbon-fiber hull, distant supernova glow",
            audioSoundDesign = "Low sub-bass rumble of fusion engines, crackle of radio static",
            action = "A lone operative in a pressurized exo-suit steps across the magnetized outer ring of the derelict station. The visor reflects the swirling purple gas clouds below.",
            dialogue = "KAI (V.O.)\n\"We were warned that the signal wasn't a distress call. It was an awakening.\""
        ),
        MovieScene(
            sceneNumber = "SCENE 02",
            heading = "INT. MAIN QUANTUM COMMAND BRIDGE - CONTINUOUS",
            visualVFX = "Flickering holographic terminal consoles, floating neon cyan schematics",
            audioSoundDesign = "Synthesizer arpeggio, rapid keystroke clicks, warning siren chime",
            action = "Kai slides open the emergency hatch. The central AI terminal boots up unprompted, displaying the glowing ASTRAM insignia.",
            dialogue = "ASTRAM CORE (AI)\n\"Identity confirmed, Operator Kai. The coordinates for the celestial anomaly have been decrypted.\""
        )
    )

    val STUDY_TOPICS = listOf(
        StudyConcept(
            topic = "Quantum Superposition & Entanglement",
            category = "Quantum Physics",
            summary = "The phenomenon where a quantum system exists in multiple states simultaneously until measured, and particles remain correlated across infinite space.",
            analogy = "Imagine a cosmic coin spinning in microgravity: while spinning, it is both heads and tails at once.",
            keyFormulas = "Ψ = α|0⟩ + β|1⟩  where  |α|² + |β|² = 1",
            keyTakeaways = listOf(
                "Superposition enables quantum bits (qubits) to process exponential data states.",
                "Entanglement links twin particles instantaneously regardless of interstellar distance.",
                "Measurement forces wave function collapse into a single observable state."
            )
        ),
        StudyConcept(
            topic = "Transformer Architecture & Self-Attention",
            category = "Artificial Intelligence",
            summary = "The deep learning foundation powering LLMs, allowing neural networks to dynamically weigh the importance of all input tokens simultaneously.",
            analogy = "Like a cosmic spotlight that simultaneously illuminates connections between every star in a constellation rather than looking at one star at a time.",
            keyFormulas = "Attention(Q, K, V) = softmax((Q Kᵀ) / √dₖ) V",
            keyTakeaways = listOf(
                "Query (Q), Key (K), and Value (V) matrices calculate contextual token affinities.",
                "Multi-Head Attention enables parallel focus on syntactic, semantic, and conceptual relationships.",
                "Positional Encodings preserve sequential order without recurrent loops."
            )
        ),
        StudyConcept(
            topic = "General Relativity & Black Hole Thermodynamics",
            category = "Astrophysics",
            summary = "Gravity described as the curvature of 4D space-time caused by mass and energy, leading to event horizons and Hawking radiation.",
            analogy = "Space-time is like a cosmic fabric of trampoline canvas, and massive stars warp the fabric into bottomless gravity wells.",
            keyFormulas = "G_μν + Λ g_μν = (8πG / c⁴) T_μν",
            keyTakeaways = listOf(
                "Mass bends light trajectories (Gravitational Lensing).",
                "Time dilates in strong gravitational fields relative to distant observers.",
                "Hawking radiation allows black holes to slowly evaporate over eons."
            )
        )
    )

    val THREE_D_MODELS = listOf(
        ThreeDModelPreset(
            name = "Tesseract 4D Hypercube",
            category = "Spatial Geometry",
            description = "A four-dimensional analog of the cube, displaying 8 cubic cells folded into hyper-dimensional space.",
            verticesCount = "16 Vertices, 32 Edges, 24 Faces, 8 Cells",
            educationalNotes = "Touch and drag to rotate the projection of 4D space into 3D cosmic viewport."
        ),
        ThreeDModelPreset(
            name = "DNA Double Helix Matrix",
            category = "Genetics & Bio-Tech",
            description = "The molecular structure of nucleic acids consisting of complementary base pairs bonded along antiparallel sugar-phosphate backbones.",
            verticesCount = "Adenine-Thymine (A-T) & Guanine-Cytosine (G-C) Nucleotide pairs",
            educationalNotes = "Observe the 3.4nm helical turn pitch and major/minor grooves in real-time 3D rotation."
        ),
        ThreeDModelPreset(
            name = "Bohr Quantum Atomic Orbiter",
            category = "Nuclear Physics",
            description = "Protons & Neutrons clustered in the dense nucleus surrounded by quantized orbital shells with probabilistic electron wave clouds.",
            verticesCount = "Multi-tier energy levels n=1, n=2, n=3 with orbital spin vectors",
            educationalNotes = "Interactive electron jump transitions emit photon wavelengths across the spectrum."
        )
    )

    val QUIZ_PRESETS = listOf(
        QuizQuestion(
            id = 1,
            topic = "Cosmic AI & Astrophysics",
            question = "What fundamental mechanism allows Transformer models to process all words in a prompt in parallel rather than sequentially?",
            options = listOf(
                "Self-Attention Matrix Computation",
                "Recurrent Backpropagation Chains",
                "Singular Value Decomposition",
                "Convolutional Max-Pooling Filters"
            ),
            correctAnswerIndex = 0,
            explanation = "Self-Attention computes dot products between Query and Key vectors for all tokens simultaneously, allowing complete parallelization on GPUs/TPUs."
        ),
        QuizQuestion(
            id = 2,
            topic = "Cosmic AI & Astrophysics",
            question = "At what boundary around a black hole is the escape velocity exactly equal to the speed of light?",
            options = listOf(
                "The Photon Sphere",
                "The Ergosphere",
                "The Event Horizon (Schwarzschild Radius)",
                "The Accretion Incline"
            ),
            correctAnswerIndex = 2,
            explanation = "The Event Horizon marks the point of no return where not even light has sufficient velocity to escape the gravitational singularity."
        ),
        QuizQuestion(
            id = 3,
            topic = "Cosmic AI & Astrophysics",
            question = "In quantum computing, what term describes two particles whose quantum states remain directly linked regardless of physical distance?",
            options = listOf(
                "Quantum Superposition",
                "Quantum Entanglement",
                "Quantum Tunneling",
                "Quantum Decoherence"
            ),
            correctAnswerIndex = 1,
            explanation = "Quantum entanglement creates an instantaneous correlation between the measurable states of paired particles."
        ),
        QuizQuestion(
            id = 4,
            topic = "Cosmic AI & Astrophysics",
            question = "Which Gemini model is optimized for deep reasoning and complex multi-step STEM tasks with high thinking mode?",
            options = listOf(
                "gemini-3.1-pro-preview",
                "gemini-3.1-flash-lite",
                "gemini-3.5-flash",
                "gemini-nano"
            ),
            correctAnswerIndex = 0,
            explanation = "gemini-3.1-pro-preview is designed for high-intelligence, multi-step problem solving, math, coding, and high thinking level tasks."
        )
    )

    fun getRandomImagePrompt(): String = AUTO_IMAGE_PRESETS.random()
    fun getRandomVideoBlueprint(): VideoBlueprint = AUTO_VIDEO_PRESETS.random()
    fun getRandomStory(): StoryChapter = AUTO_STORY_PRESETS.random()
    fun getRandomMovie(): MovieScene = AUTO_MOVIE_PRESETS.random()
    fun getRandomStudy(): StudyConcept = STUDY_TOPICS.random()
    fun getRandom3DModel(): ThreeDModelPreset = THREE_D_MODELS.random()
}

data class VideoBlueprint(
    val title: String,
    val cameraMotion: String,
    val framerate: String,
    val aspectRatio: String,
    val lighting: String,
    val scenePrompt: String,
    val duration: String
)

data class StoryChapter(
    val chapterTitle: String,
    val setting: String,
    val narrative: String,
    val choices: List<String>
)

data class MovieScene(
    val sceneNumber: String,
    val heading: String,
    val visualVFX: String,
    val audioSoundDesign: String,
    val action: String,
    val dialogue: String
)

data class StudyConcept(
    val topic: String,
    val category: String,
    val summary: String,
    val analogy: String,
    val keyFormulas: String,
    val keyTakeaways: List<String>
)

data class ThreeDModelPreset(
    val name: String,
    val category: String,
    val description: String,
    val verticesCount: String,
    val educationalNotes: String
)

data class QuizQuestion(
    val id: Int,
    val topic: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)
