# TP noté - Bonne pratique de code

## Damien CARRIER - INFRES 12

### Partie persistence

#### TreeRepository

* TreeRepository : 

```java
    void delete(UUID id);
    
    Tree update(Tree tree);
```

* TreeRepositoryImpl : 

```java
    @Override
    public void delete(UUID id) throws NoSuchElementException {
        final Optional<Tree> persisted = findOneById(id);

        if (persisted.isPresent()) {
            mutableRepository.remove(persisted);
        } else {
            throw new NoSuchElementException("Tree with id [" + id + "] does not exists");
        }
    }

    @Override
    public Tree update(Tree tree) throws NoSuchElementException {
        final Optional<Tree> persistedTree = findOneById(tree.id());

        if (persistedTree.isPresent()) {
            int index = mutableRepository.indexOf(tree);
            mutableRepository.set(index, tree);
        } else {
            throw new NoSuchElementException("Tree with id [" + tree.id() + "] does not exists");
        }

        return tree;
    }
```

Pour chaqu'une de ces deux méthodes, je vérifie que l'arbre existe, s'il n'existe pas on renvoie une exception sinon on le supprime ou le modifie en fonction de la méthode appelée.

#### ForestRepository

* ForestRepository

```java
public interface ForestRepository {
    Optional<Forest> findOneById(UUID id);

    List<Forest> findAll();

    UUID insert(Forest forest);

    void delete(UUID id);

    Forest update(Forest forest);

    List<Species> getAllSpeciesByForest(UUID id);
}
```

* InMemoryForestRepositoryImpl

```java
@Service
public class InMemoryForestRepositoryImpl implements ForestRepository {
    private List<Forest> mutableRepository = new ArrayList<>();

    @Override
    public Optional<Forest> findOneById(UUID id) {
        return mutableRepository.stream()
                .filter(forest -> id.equals(forest.id()))
                .findFirst();
    }

    @Override
    public List<Forest> findAll() {
        return mutableRepository;
    }

    @Override
    public UUID insert(Forest forest) {
        final Forest persisted = new Forest(
                UUID.randomUUID(),
                forest.type(),
                forest.trees(),
                forest.surface()
        );
        mutableRepository.add(persisted);

        return persisted.id();
    }

    @Override
    public void delete(UUID id) throws NoSuchElementException {
        final Optional<Forest> persisted = findOneById(id);

        if (persisted.isPresent()) {
            mutableRepository.remove(persisted);
        } else {
            throw new NoSuchElementException("Tree with id [" + id + "] does not exists");
        }
    }

    @Override
    public Forest update(Forest forest) throws NoSuchElementException {
        final Optional<Forest> persistedForest = findOneById(forest.id());

        if (persistedForest.isPresent()) {
            int index = mutableRepository.indexOf(forest);
            mutableRepository.set(index, forest);
        } else {
            throw new NoSuchElementException("Tree with id [" + forest.id() + "] does not exists");
        }

        return forest;
    }

    @Override
    public List<Species> getAllSpeciesByForest(UUID id) throws NoSuchElementException {
        final Optional<Forest> forest = findOneById(id);

        if (forest.isPresent()) {
            return forest.get().trees().stream().map(Tree::species).distinct().collect(Collectors.toList());
        } else {
            throw new NoSuchElementException("Tree with id [" + id + "] does not exists");
        }
    }
}
```

Les méthodes ici sont inspirés de celle du TreeRepository, j'ai rajouter la méthodes *getAllSpeciesByForest* qui permet de récupérer toutes les espèces d'une forêt



### Partie service

#### TreeService

* TreeService

```java
    UUID add(Tree tree);    
    
    void deleteTree(UUID id);

    Tree updateTree(Tree tree);
```

* TreeServiceImpl

```java
    @Override
    public UUID add(Tree tree) {
        return this.treeRepository.insert(tree);
    }
    
    @Override
    public void deleteTree(UUID id) {
        this.treeRepository.delete(id);
    }

    @Override
    public Tree updateTree(Tree tree) {
        return this.treeRepository.update(tree);
    }
```

Ajout des deux méthodes permettant de modifer et supprimer un arbre.

* TreeServiceImplTest

```java
    @Test
    void shouldGetAll() {
        // GIVEN
        when(treeRepository.findAll()).thenReturn(List.of(
                new Tree(UUID.randomUUID(), LocalDate.now(), Species.OAK, Exposure.SHADOW, 40.0),
                new Tree(UUID.randomUUID(), LocalDate.now(), Species.OAK, Exposure.SHADOW, 40.0)
        ));

        // WHEN
        List<Tree> all = treeService.list();

        // THEN
        assertEquals(2, all.size());
    }

    @Test
    void shouldSaveATree() {
        UUID generatedId = UUID.randomUUID();
        Tree newTree = new Tree(generatedId, LocalDate.now(), Species.OAK, Exposure.SHADOW, 40.0);

        when(treeRepository.insert(any(Tree.class))).thenReturn(newTree.id());

        assertEquals(generatedId, treeService.add(newTree));
    }

    @Test
    void shouldUpdateATree() {
        UUID generatedId = UUID.randomUUID();
        Tree updatedTree = new Tree(generatedId, LocalDate.now(), Species.FIR, Exposure.SHADOW, 40.0);

        // WHEN
        treeService.updateTree(updatedTree);

        // THEN
        verify(treeRepository, times(1)).update(updatedTree);
    }

    @Test
    void shouldDeleteATree(){
        // GIVEN
        UUID uuid = UUID.randomUUID();

        // WHEN
        treeService.deleteTree(uuid);

        // THEN
        verify(treeRepository, times(1)).delete(uuid);
    }
```

#### ForestService

* ForestService

```java
public interface ForestService {

    Optional<Forest> get(UUID uuid);

    List<Forest> list();

    UUID add(Forest forest);

    void deleteForest(UUID id);

    Forest updateForest(Forest forest);

    List<Species> getForestSpecies(UUID id);
}
```

* ForestServiceImpl

```java
public class ForestServiceImpl implements ForestService {
    private ForestRepository forestRepository;

    @Autowired
    public ForestServiceImpl(ForestRepository forestRepository) {
        this.forestRepository = forestRepository;
    }

    @Override
    public Optional<Forest> get(UUID uuid) {
        return this.forestRepository.findOneById(uuid);
    }

    @Override
    public List<Forest> list() {
        return this.forestRepository.findAll();
    }

    @Override
    public UUID add(Forest forest) {
        return this.forestRepository.insert(forest);
    }

    @Override
    public void deleteForest(UUID id) {
        this.forestRepository.delete(id);
    }

    @Override
    public Forest updateForest(Forest forest) {
        return this.forestRepository.update(forest);
    }

    @Override
    public List<Species> getForestSpecies(UUID id) {
        return this.forestRepository.getAllSpeciesByForest(id);
    }
}
```

Implémentation des méthodes qui font appel à celle définis dans le repository.

* ForestServiceImplTest

```java
@ExtendWith(MockitoExtension.class)
public class ForestServiceImplTest {
    @Mock
    private ForestRepository forestRepository;

    @InjectMocks
    private ForestService forestService = new ForestServiceImpl(forestRepository);

    @Test
    void shouldGetATree(){
        // GIVEN
        final UUID uuid = UUID.randomUUID();
        final List<Tree> trees = new ArrayList<>();
        final Forest repositoryForest = new Forest(uuid, ForestType.TROPICAL, trees, 100000.00);
        when(forestRepository.findOneById(uuid)).thenReturn(Optional.of(repositoryForest));

        // WHEN
        Optional<Forest> forest = forestService.get(uuid);

        // THEN
        assertTrue(forest.isPresent());
        assertEquals(uuid, forest.map(Forest::id).get());
    }

    @Test
    void shouldGetAll() {
        // GIVEN
        final List<Tree> trees = new ArrayList<>();

        when(forestRepository.findAll()).thenReturn(List.of(
                new Forest(UUID.randomUUID(), ForestType.TROPICAL, trees, 100000.00),
                new Forest(UUID.randomUUID(), ForestType.BOREAL, trees, 100000.00)
        ));

        // WHEN
        List<Forest> all = forestService.list();

        // THEN
        assertEquals(2, all.size());
    }

    @Test
    void shouldSaveATree() {
        UUID generatedId = UUID.randomUUID();
        final List<Tree> trees = new ArrayList<>();
        Forest newForest = new Forest(generatedId, ForestType.TROPICAL, trees, 100000.00);

        when(forestRepository.insert(any(Forest.class))).thenReturn(newForest.id());

        assertEquals(generatedId, forestService.add(newForest));
    }

    @Test
    void shouldUpdateATree() {
        UUID generatedId = UUID.randomUUID();
        final List<Tree> trees = new ArrayList<>();
        Forest forest = new Forest(generatedId, ForestType.TROPICAL, trees, 100000.00);

        // WHEN
        forestService.updateForest(forest);

        // THEN
        verify(forestRepository, times(1)).update(forest);
    }

    @Test
    void shouldDeleteATree() {
        // GIVEN
        UUID uuid = UUID.randomUUID();

        // WHEN
        forestService.deleteForest(uuid);

        // THEN
        verify(forestRepository, times(1)).delete(uuid);
    }

    @Test
    void shouldGetAllSpecies() {
        // GIVEN
        UUID generatedId = UUID.randomUUID();

        //WHEN
        forestService.getForestSpecies(generatedId);

        //THEN
        verify(forestRepository, times(1)).getAllSpeciesByForest(generatedId);
    }
}
```

### Calcul de la capacité d'absorption d'une forêt

```java
    @Override
    public double getSumCapacityStorageForest(UUID id) {
        Optional<Forest> forest = this.get(id);

        if (forest.isPresent()) {
            return forest.get().trees().stream().map(Tree::carbonStorageCapacity)
                    .mapToDouble(a -> a)
                    .sum();
        }

        throw new NoSuchElementException("Forest with id [" + id + "] does not exists");
    }

    @Override
    public double getForestAbsorbtionCapacity(UUID id) {
        int numberOfSpecies = this.getForestSpecies(id).size();

        return 0.4 * numberOfSpecies * this.getSumCapacityStorageForest(id);
    }
```

J'applique un coefficient choisit aléatoirement pour calculer la capacité d'absorbtion.
