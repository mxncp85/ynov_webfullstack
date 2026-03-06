-- Insérer des données d'exemple dans la table aventuriers
INSERT INTO aventuriers (id, nom, description, niveau, classe, caracteristiques_physique, caracteristiques_mental, caracteristiques_perception, created_at, updated_at) 
VALUES 
  ('550e8400-e29b-41d4-a716-446655440001', 'Aragorn', 'Rôdeur des terres sauvages, héritier du trône du Gondor', 5, 'RODEUR', 17, 14, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440002', 'Legolas', 'Elfe de la Forêt Noire, archer d''exception', 3, 'RODEUR', 14, 13, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440003', 'Gimli', 'Guerrier nain de Durin''s folk', 4, 'GUERRIER', 16, 12, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440004', 'Gandalf', 'Magicien gris, guide de la Communauté', 7, 'MAGE', 11, 19, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440005', 'Frodo', 'Jeune hobbit du Pays de Comté', 2, 'VOLEUR', 8, 11, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440006', 'Boromir', 'Guerrier du Gondor, fils du Seigneur', 6, 'GUERRIER', 18, 13, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440007', 'Elrond', 'Maître d''Imladris, sage et puissant', 8, 'CLERC', 15, 18, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
