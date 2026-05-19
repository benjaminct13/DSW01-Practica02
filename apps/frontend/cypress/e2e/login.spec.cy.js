describe('Login page', () => {
  it('should load the login page', () => {
    cy.visit('/login');
    cy.get('form').should('exist');
  });
});
